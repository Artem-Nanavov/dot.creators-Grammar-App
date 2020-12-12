import spacy
from flask import Flask, request, jsonify
from parser import parser


app = Flask(__name__)


def spacyInstallizing(spacyLanguage):
    entities = spacyLanguage.create_pipe("merge_entities")
    chunks = spacyLanguage.create_pipe("merge_noun_chunks")
    spacyLanguage.add_pipe(entities, chunks)
    return spacyLanguage
grammarTextGetter = spacyInstallizing(spacy.load("en_core_web_sm"))




def spacySearchText():
    pass

# Get html
@app.route('/')
@app.route('/get-html', methods=["POST"])
def index():
    gettedJson = request.get_json()
    print(gettedJson)
    for i in range(2):
        print()
    with open('/Users/controldata/PycharmProjects/-dot.creators-Grammar-App/server/html-text.txt', 'w') as txtFile:
        txtFile.writelines(parser(gettedJson['html']))
    print(parser(gettedJson['html']))
    return jsonify(gettedJson)

@app.route('/get', methods=["GET"])
def helloWorld():
    return "Hello world"
if __name__ == '__main__':
    app.debug = True
    app.run(host = '192.168.0.14',port=5000)