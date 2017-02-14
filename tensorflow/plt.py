#!/usr/bin/env python3

import matplotlib.pyplot as plt
import numpy as np

x = np.linspace(-3, 3, 50)
y1 = 2 * x + 1
y2 = x ** 2

plt.figure()
l1, = plt.plot(x, y1, label = 'up')
l2, = plt.plot(x, y2, color = 'red', linewidth = 2.0, linestyle = '--', label = 'down')
plt.xlim((-3, 3))
plt.ylim((-3, 5))
plt.xlabel('I am x')
plt.ylabel('I am y')
ticks = np.linspace(-3, 3, 7)
plt.xticks(ticks)
plt.yticks([-2, -1.5, -1, 1.22, 3],
	       ['really bad', 'bad', 'normal', r'$good\ \alpha$', r'$really\ good$'])
plt.legend(handles = [l1, l2], labels = ['aaa', 'bbb'], loc = 'best')

ax = plt.gca()
ax.spines['right'].set_color('none')
ax.spines['top'].set_color('none')
ax.xaxis.set_ticks_position('bottom')
ax.yaxis.set_ticks_position('left')
ax.spines['bottom'].set_position(('data', 0))
ax.spines['left'].set_position(('data', 0))

x0 = 1
y0 = 2 * x0 + 1
plt.scatter(x0, y0, s = 50, color = 'b')
plt.plot([x0, x0], [y0, 0], 'k--', lw = 2.5)

plt.annotate(r'$2x + 1 = %s$' % y0, xy = (x0, y0), xycoords = 'data', xytext = (+30, -30), textcoords = 'offset points',
	fontsize = 16, arrowprops = dict(arrowstyle = '->', connectionstyle = 'arc3, rad = .2'))
plt.text(-2.7, 4, r'$This\ is\ the\ some\ text.\ \mu\ \sigma_i\ \alpha_t$', fontdict = {'size': 16, 'color': 'r'})

for label in ax.get_xticklabels() + ax.get_yticklabels():
	label.set_fontsize(12)
	label.set_bbox(dict(facecolor = 'yellow', edgecolor = 'None', alpha = 0.7))

plt.show()
