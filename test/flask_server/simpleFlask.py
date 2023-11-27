import unittest
from flask import Flask
from flask.testing import FlaskClient
from flask_cors import CORS, cross_origin
import subprocess

class SecurityTest(unittest.TestCase):

    def setUp(self):
        self.app = Flask(__name__)
        self.app.testing = True
        self.client = self.app.test_client()
        CORS(self.app)

    def test_command_injection(self):
        response = self.client.post('/simpleApi', json={'name': '; ls'})
        self.assertEqual(response.status_code, 200)
        self.assertNotIn('user ; ls created!', response.get_data(as_text=True))

if __name__ == '__main__':
    unittest.main()