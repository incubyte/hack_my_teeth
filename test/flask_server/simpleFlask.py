import unittest
from flask import Flask
from flask.testing import FlaskClient
from flask_cors import CORS, cross_origin
import subprocess

class TestApp(unittest.TestCase):

    def setUp(self):
        self.app = Flask(__name__)
        self.app.config['TESTING'] = True
        self.client = self.app.test_client()
        CORS(self.app)

    def test_postSimpleApi(self):
        data = {
            'name': 'John Doe',
            'phone': '1234567890'
        }
        response = self.client.post('/simpleApi', json=data)
        self.assertEqual(response.status_code, 200)
        self.assertIn(b'user John Doe created!', response.data)

    def test_getSimpleApi(self):
        response = self.client.get('/simpleApi/1')
        self.assertEqual(response.status_code, 200)
        self.assertIn(b'Ardyanto Songoku', response.data)
        self.assertIn(b'0912221121', response.data)

    def test_getAuthenApi_authenticated(self):
        with self.client as c:
            with c.session_transaction() as session:
                session['pageCookie'] = '1'
            response = c.get('/authenApi/1')
        self.assertEqual(response.status_code, 200)
        self.assertIn(b'Ardyanto Songoku', response.data)
        self.assertIn(b'0912221121', response.data)
        self.assertIn(b'khct9ok4', response.data)

    def test_getAuthenApi_not_authenticated(self):
        response = self.client.get('/authenApi/1')
        self.assertEqual(response.status_code, 400)
        self.assertIn(b'Not authenticated', response.data)

    def test_getVulAuthenApi_authenticated(self):
        with self.client as c:
            with c.session_transaction() as session:
                session['pageCookie'] = '1'
            response = c.get('/vulauthenApi/1')
        self.assertEqual(response.status_code, 200)
        self.assertIn(b'Ardyanto Songoku', response.data)
        self.assertIn(b'0912221121', response.data)
        self.assertIn(b'khct9ok4', response.data)

    def test_getVulAuthenApi_not_authenticated(self):
        response = self.client.get('/vulauthenApi/1')
        self.assertEqual(response.status_code, 400)
        self.assertIn(b'Not authenticated', response.data)

if __name__ == '__main__':
    unittest.main()