#!/usr/bin/env python3

import os
import gi

gi.require_version('Gst', '1.0')
from gi.repository import Gst, GObject, Gtk
from gi.repository import GdkX11, GstVideo

class GTK_Main(object):
    def __init__(self):
        window = Gtk.Window(Gtk.WindowType.TOPLEVEL)
        window.set_title("Webcam-Viewer")
        window.set_default_size(640, 360)
        #window.set_default_size(800, 448)
        window.connect("destroy", Gtk.main_quit, "WM destroy")
        vbox = Gtk.VBox()
        window.add(vbox)
        self.movie_window = Gtk.DrawingArea()
        vbox.add(self.movie_window)
        hbox = Gtk.HBox()
        vbox.pack_start(hbox, False, False, 0)
        hbox.set_border_width(10)
        hbox.pack_start(Gtk.Label(), False, False, 0)
        self.button = Gtk.Button("Start")
        self.button.connect("clicked", self.start_stop)
        hbox.pack_start(self.button, False, False, 0)
        self.button2 = Gtk.Button("Quit")
        self.button2.connect("clicked", self.exit)
        hbox.pack_start(self.button2, False, False, 0)
        hbox.add(Gtk.Label())
        window.show_all()
        self.xid = self.movie_window.get_property('window').get_xid()

        self.player = Gst.Pipeline.new("player")
        source = Gst.ElementFactory.make("uvch264src", "video-source")
        source.set_property("initial-bitrate", 3000000)
        source.set_property("average-bitrate", 3000000)
        source.set_property("peak-bitrate", 3000000)
        source.set_property("rate-control", "cbr")
        source.set_property("entropy", "cabac")
        source.set_property("iframe-period", 2000)
        source.set_property("device", "/dev/video0")
        source.set_property("auto-start", True)

        queue_src1 = Gst.ElementFactory.make("queue", "queue_src1")
        caps_src1 = Gst.Caps.from_string("video/x-raw, format=YUY2, width=320, height=240, framerate=10/1")
        filter_src1 = Gst.ElementFactory.make("capsfilter", "filter_src1")
        filter_src1.set_property("caps", caps_src1)

        queue_src2 = Gst.ElementFactory.make("queue", "queue_src2")
        caps_src2 = Gst.Caps.from_string("video/x-h264, width=640, height=360, framerate=24/1, stream-format=byte-stream, profile=high")
        #caps_src2 = Gst.Caps.from_string("video/x-h264, width=800, height=448, framerate=24/1, stream-format=byte-stream, profile=high")
        filter_src2 = Gst.ElementFactory.make("capsfilter", "filter_src2")
        filter_src2.set_property("caps", caps_src2)

        parse = Gst.ElementFactory.make("h264parse", "video-parse")
        h264_dec = Gst.ElementFactory.make("avdec_h264", "h264_dec")
        mux = Gst.ElementFactory.make("flvmux", "mux")
        mux.set_property("streamable", True)
        #decoder = Gst.ElementFactory.make("decodebin", "decoder")
        #decoder.connect("pad-added", self.decoder_callback)

        audio = Gst.ElementFactory.make("alsasrc", "audio-source")
        audio.set_property("device", "hw:1")
        volume = Gst.ElementFactory.make("volume", "vol")
        volume.set_property("volume", 0.9)
        aacenc = Gst.ElementFactory.make("voaacenc", "aac")
        aacenc.set_property("bitrate", 128000)

        queue_src3 = Gst.ElementFactory.make("queue", "queue_src3")
        caps_src3 = Gst.Caps.from_string("audio/x-raw, format=S16LE, rate=32000, channels=2")
        filter_src3 = Gst.ElementFactory.make("capsfilter", "filter_src3")
        filter_src3.set_property("caps", caps_src3)

        queue_video = Gst.ElementFactory.make("queue", "queue_video")
        queue_audio = Gst.ElementFactory.make("queue", "queue_audio")
        queue_audio.set_property("max-size-buffers", 6000)
        queue_audio.set_property("max-size-time", 0)
        queue_audio.set_property("max-size-bytes", 0)
        queue_audio.set_property("flush-on-eos", True)

        fakesink = Gst.ElementFactory.make("fakesink", "fake-output")
        fakesink.set_property("sync", False)
        self.colorspace = Gst.ElementFactory.make("autovideoconvert", "colorspace")
        videosink = Gst.ElementFactory.make("ximagesink", "video-output")
        videosink.set_property("sync", False)
        self.audioconv = Gst.ElementFactory.make("audioconvert", "converter")
        audiosink = Gst.ElementFactory.make("autoaudiosink", "audio-output")

        for ele in (source, queue_src1, filter_src1, queue_src2, filter_src2, parse, h264_dec,
                    queue_video, fakesink, self.colorspace, videosink):
            self.player.add(ele)

        """
        for ele in (source, queue_src1, filter_src1, queue_src2, filter_src2, parse, h264_dec, mux,
                    decoder, audio, volume, aacenc, queue_src3, filter_src3, queue_video, queue_audio,
                    fakesink, self.colorspace, videosink, self.audioconv, audiosink):
            self.player.add(ele)
        """

        source.get_static_pad("vfsrc").link(queue_src1.get_static_pad("sink"))
        queue_src1.link(filter_src1)
        filter_src1.link(fakesink)

        source.get_static_pad("vidsrc").link(queue_src2.get_static_pad("sink"))
        queue_src2.link(filter_src2)
        filter_src2.link(parse)
        parse.link(queue_video)

        queue_video.link(h264_dec)
        h264_dec.link(self.colorspace)
        #video_pad = mux.get_request_pad("video")
        #queue_video.get_static_pad("src").link(video_pad)

        """
        audio.link(queue_src3)
        queue_src3.link(volume)
        volume.link(filter_src3)
        filter_src3.link(aacenc)
        aacenc.link(queue_audio)
        audio_pad = mux.get_request_pad("audio")
        queue_audio.get_static_pad("src").link(audio_pad)
        mux.link(decoder)
        """

        self.colorspace.link(videosink)
        #self.audioconv.link(audiosink)

        bus = self.player.get_bus()
        bus.add_signal_watch()
        bus.enable_sync_message_emission()
        bus.connect("message", self.on_message)
        bus.connect("sync-message::element", self.on_sync_message)

    def start_stop(self, w):
        if self.button.get_label() == "Start":
            self.button.set_label("Stop")
            self.player.set_state(Gst.State.PLAYING)
        else:
            self.player.set_state(Gst.State.NULL)
            self.button.set_label("Start")

    def exit(self, widget, data = None):
        Gtk.main_quit()

    def on_message(self, bus, message):
        t = message.type
        if t == Gst.MessageType.EOS:
            self.player.set_state(Gst.State.NULL)
            self.button.set_label("Start")
        elif t == Gst.MessageType.ERROR:
            err, debug = message.parse_error()
            print("Error: %s" % err, debug)
            self.player.set_state(Gst.State.NULL)
            self.button.set_label("Start")

    def on_sync_message(self, bus, message):
        if message.get_structure().get_name() == 'prepare-window-handle':
            imagesink = message.src
            imagesink.set_property("force-aspect-ratio", True)
            imagesink.set_window_handle(self.xid)

    """
    def decoder_callback(self, decoder, pad):
        caps = pad.query_caps(None)
        structure_name = caps.to_string()
        if structure_name.startswith("video"):
            video_pad = self.colorspace.get_static_pad("sink")
            pad.link(video_pad)
        elif structure_name.startswith("audio"):
            audio_pad = self.audioconv.get_static_pad("sink")
            pad.link(audio_pad)
    """

Gst.init(None)
GTK_Main()
GObject.threads_init()
Gtk.main()
