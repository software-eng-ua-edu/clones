from xml.dom.minidom import parse
import sys

fileToConvert = sys.argv[1]
fileToWrite = sys.argv[2]

dom = parse(fileToConvert)
root = dom.documentElement

i = 1
for ele in root.getElementsByTagName("clone"):
    ele.tagName = u'class'
    ele.setAttribute("id", str(i))
    i += 1

for ele in root.getElementsByTagName("cloneinfo"):
    ele.tagName = u'classinfo'

f = open(fileToWrite, 'w')
f.write(dom.toxml())
