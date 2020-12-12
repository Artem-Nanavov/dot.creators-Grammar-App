from parser import parser
def getActiveTenseRule(tense):
    tenseList = {
        "ALL": dict(aux=["do", "does", "did", "am", "is", "are", "have", "has", "was", "were", "had", "shall", "will",
                      "would", "should", "be"],
                    vtag=["VB", "VBZ", "VBP", "VBG", "VBN", "VBD"])
    }


def searchBatchesActiveVoice(grammarTextGetter, text, grammarRule):
    text = parser(text)
