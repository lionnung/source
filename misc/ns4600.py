#!/usr/bin/env python3

import socket

def wake(mac_address):
    """Wake the NAS when Wake-on-LAN is enabled on the device."""

    # convert to raw bytes
    hex_data = mac_address.split(':')
    raw_mac_bytes = ''.join([chr(int(item, 16)) for item in hex_data])
    magic_packet = '\xff' * 6 + raw_mac_bytes * 16

    # broadcast magic packet on LAN
    broadcast_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    broadcast_socket.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
    broadcast_socket.sendto(magic_packet.encode('latin1'), ("255.255.255.255", 9))
    broadcast_socket.close()

if __name__ == '__main__':
    wake('00:01:55:11:95:55')
    print('Wake the NAS up...')
