import flask
from flask_cors import CORS, cross_origin
import subprocess
app=flask.Flask(__name__,template_folder="/code")
cors = CORS(app)
dict_user= {"1":{"name":"Ardyanto Songoku","phone":"0912221121","salt":"khct9ok4"},"2":{"name":"Tin Tran","phone":"093332221","salt":"9bqtepv0"}}
count=2
@app.route("/",methods=["GET"])
def index():
    text = open('/code/front.txt', 'r+')
    content = text.read()
    text.close()
    resp = flask.make_response(flask.render_template('template.html', content=content))
    resp.set_cookie('pageCookie', '1')
    return resp 

@app.route("/simpleApi",methods=["POST"])
def postSimpleApi():
    content = flask.request.json
    text = subprocess.getoutput('echo user ' + content['name'] + ' created!')
    e = int(max(dict_user))
    dict_user[str(e+1)] = flask.request.json
    return flask.make_response(text)

@app.route("/simpleApi/<userid>",methods=["GET"])
def getSimpleApi(userid):
    return flask.jsonify(
        name=dict_user[userid]["name"],
        phone=dict_user[userid]["phone"]
    )



@app.route("/simpleApi",methods=["GET"])
def generatePage():
    return flask.render_template('simpleApi.html')
'''
@app.route("/authenApi/<userid>",methods=["GET"])
@cross_origin(origins="http://app.hackteeth.com",allow_headers=['Content-Type'],supports_credentials=True)
def getAuthenApi(userid):
    return flask.jsonify(
        name=dict_user[userid]["name"],
        phone=dict_user[userid]["phone"],
        salt=dict_user[userid]["salt"]
    )
'''
def bad_request(message):
    response = flask.jsonify({'message': message})
    response.status_code = 400
    return response
@app.route("/authenApi/<userid>",methods=["GET"])
@cross_origin(origins="http://app.hackteeth.com",allow_headers=['Content-Type'],supports_credentials=True)
def getAuthenApi(userid):
    cookie = flask.request.cookies.get('pageCookie')
    if(cookie != None):
            return flask.jsonify(
            name=dict_user[userid]["name"],
            phone=dict_user[userid]["phone"],
            salt=dict_user[userid]["salt"]
        )
    else:
        return bad_request('Not authenticated')
@app.route("/vulauthenApi/<userid>",methods=["GET"])
@cross_origin(allow_headers=['Content-Type'],supports_credentials=True)
def getVulAuthenApi(userid):
    cookie = flask.request.cookies.get('pageCookie')
    if(cookie != None):
            return flask.jsonify(
            name=dict_user[userid]["name"],
            phone=dict_user[userid]["phone"],
            salt=dict_user[userid]["salt"]
        )
    else:
        return bad_request('Not authenticated')
if __name__ == "__main__":
    app.run( host='0.0.0.0', port=5000, debug = True)


