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

    def test_postSimpleApi_command_injection(self):
        payload = {
            'name': '; ls'
        }
        response = self.client.post('/simpleApi', json=payload)
        self.assertEqual(response.status_code, 200)
        self.assertNotIn('user ; ls created!', response.get_data(as_text=True))

if __name__ == '__main__':
    unittest.main()