import unittest
from unittest.mock import patch
import subprocess

import flask
from flask_cors import CORS, cross_origin

app = flask.Flask(__name__, template_folder="/code")
cors = CORS(app)
dict_user = {
    "1": {"name": "Ardyanto Songoku", "phone": "0912221121", "salt": "khct9ok4"},
    "2": {"name": "Tin Tran", "phone": "093332221", "salt": "9bqtepv0"}
}
count = 2


@app.route("/", methods=["GET"])
def index():
    text = open('/code/front.txt', 'r+')
    content = text.read()
    text.close()
    resp = flask.make_response(flask.render_template('template.html', content=content))
    resp.set_cookie('pageCookie', '1')
    return resp


@app.route("/simpleApi", methods=["POST"])
def postSimpleApi():
    content = flask.request.json
    text = subprocess.getoutput('echo user ' + content['name'] + ' created!')
    e = int(max(dict_user))
    dict_user[str(e + 1)] = flask.request.json
    return flask.make_response(text)


@app.route("/simpleApi/<userid>", methods=["GET"])
def getSimpleApi(userid):
    return flask.jsonify(
        name=dict_user[userid]["name"],
        phone=dict_user[userid]["phone"]
    )


@app.route("/simpleApi", methods=["GET"])
def generatePage():
    return flask.render_template('simpleApi.html')


def bad_request(message):
    response = flask.jsonify({'message': message})
    response.status_code = 400
    return response


@app.route("/authenApi/<userid>", methods=["GET"])
@cross_origin(origins="http://app.hackteeth.com", allow_headers=['Content-Type'], supports_credentials=True)
def getAuthenApi(userid):
    cookie = flask.request.cookies.get('pageCookie')
    if cookie != None:
        return flask.jsonify(
            name=dict_user[userid]["name"],
            phone=dict_user[userid]["phone"],
            salt=dict_user[userid]["salt"]
        )
    else:
        return bad_request('Not authenticated')


@app.route("/vulauthenApi/<userid>", methods=["GET"])
@cross_origin(allow_headers=['Content-Type'], supports_credentials=True)
def getVulAuthenApi(userid):
    cookie = flask.request.cookies.get('pageCookie')
    if cookie != None:
        return flask.jsonify(
            name=dict_user[userid]["name"],
            phone=dict_user[userid]["phone"],
            salt=dict_user[userid]["salt"]
        )
    else:
        return bad_request('Not authenticated')


class TestApp(unittest.TestCase):
    def setUp(self):
        self.app = app.test_client()

    def test_postSimpleApi(self):
        with patch('subprocess.getoutput') as mock_getoutput:
            mock_getoutput.return_value = 'user TestUser created!'
            response = self.app.post('/simpleApi', json={'name': 'TestUser'})
            self.assertEqual(response.status_code, 200)
            self.assertEqual(response.data.decode(), 'user TestUser created!')

    def test_getSimpleApi(self):
        response = self.app.get('/simpleApi/1')
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json, {'name': 'Ardyanto Songoku', 'phone': '0912221121'})

    def test_generatePage(self):
        response = self.app.get('/simpleApi')
        self.assertEqual(response.status_code, 200)
        self.assertIn('simpleApi.html', response.data.decode())

    def test_getAuthenApi_authenticated(self):
        with patch('flask.request.cookies.get') as mock_get_cookie:
            mock_get_cookie.return_value = '1'
            response = self.app.get('/authenApi/1')
            self.assertEqual(response.status_code, 200)
            self.assertEqual(response.json, {'name': 'Ardyanto Songoku', 'phone': '0912221121', 'salt': 'khct9ok4'})

    def test_getAuthenApi_not_authenticated(self):
        with patch('flask.request.cookies.get') as mock_get_cookie:
            mock_get_cookie.return_value = None
            response = self.app.get('/authenApi/1')
            self.assertEqual(response.status_code, 400)
            self.assertEqual(response.json, {'message': 'Not authenticated'})

    def test_getVulAuthenApi_authenticated(self):
        with patch('flask.request.cookies.get') as mock_get_cookie:
            mock_get_cookie.return_value = '1'
            response = self.app.get('/vulauthenApi/1')
            self.assertEqual(response.status_code, 200)
            self.assertEqual(response.json, {'name': 'Ardyanto Songoku', 'phone': '0912221121', 'salt': 'khct9ok4'})

    def test_getVulAuthenApi_not_authenticated(self):
        with patch('flask.request.cookies.get') as mock_get_cookie:
            mock_get_cookie.return_value = None
            response = self.app.get('/vulauthenApi/1')
            self.assertEqual(response.status_code, 400)
            self.assertEqual(response.json, {'message': 'Not authenticated'})


if __name__ == "__main__":
    unittest.main()