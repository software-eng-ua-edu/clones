from xml.dom.minidom import parse
import re
import sys

fileToConvert = sys.argv[1]
fileToWrite = sys.argv[2]
orig = sys.argv[3]
new = sys.argv[4]

dom = parse(fileToConvert)
root = dom.documentElement

for ele in root.getElementsByTagName("source"):
    path = ele.getAttribute("file")
    ele.setAttribute("file", re.sub("^" + orig, new, path))

f = open(fileToWrite, 'w')
f.write(dom.toxml())
