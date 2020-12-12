import spacy
from flask import Flask, request, jsonify
from parser import parser
from spacyLogic import searchBatchesActiveVoice

app = Flask(__name__)


def spacyInstallizing(spacyLanguage):
    entities = spacyLanguage.create_pipe("merge_entities")
    chunks = spacyLanguage.create_pipe("merge_noun_chunks")
    spacyLanguage.add_pipe(entities, chunks)
    return spacyLanguage
grammarTextGetter = spacyInstallizing(spacy.load("en_core_web_sm"))
# print(searchBatchesActiveVoice(grammarTextGetter, parser.text1, 'PAST_SIMPLE'))


@app.route('/')
@app.route('/get-text', methods=["POST"])
def index():
    gettedJson = request.get_json()
    print(gettedJson, '\n')
    text = parser(gettedJson['text'])

    with open('/Users/controldata/PycharmProjects/-dot.creators-Grammar-App/server/rawText.txt', 'w') as textFile:
        textFile.writelines(text)

    print(parser(gettedJson['text']))
    print(searchBatchesActiveVoice(grammarTextGetter, text, 'PAST_SIMPLE'))
    with open('/Users/controldata/PycharmProjects/-dot.creators-Grammar-App/server/parsedText.txt',
              'w') as parsedTextFile:
        parsedTextFile.writelines(str(searchBatchesActiveVoice(grammarTextGetter, text, 'PAST_SIMPLE')))

    return jsonify(gettedJson)


if __name__ == '__main__':
    app.debug = True
    app.run(host='192.168.0.14', port=5000)
