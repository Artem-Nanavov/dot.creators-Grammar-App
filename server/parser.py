import os
import re


def parser(text):
    text = os.linesep.join([s for s in text.splitlines() if s])

    text = text.replace(u"\u1427", ".")

    replaceList = [
        u"\u002d", u"\u058a", u"\u058b", u"\u2010", u"\u2010", u"\u2012", u"\u2013",
        u"\u2014", u"\u2015", u"\u2e3a", u"\u2e3b", u"\ufe58", u"\ufe63", u"\uff0d",
    ]
    for item in replaceList:
        text = text.replace(item, "-")
    return text