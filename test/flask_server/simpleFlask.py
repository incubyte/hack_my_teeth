import unittest
from unittest.mock import patch
import subprocess

class TestApp(unittest.TestCase):

    def test_command_injection(self):
        content = {'name': 'test'}
        expected_output = 'user test created!'
        
        with patch('subprocess.getoutput') as mock_getoutput:
            mock_getoutput.return_value = expected_output
            
            response = self.app.post('/simpleApi', json=content)
            
            self.assertEqual(response.status_code, 200)
            self.assertEqual(response.get_data(as_text=True), expected_output)
            mock_getoutput.assert_called_once_with(['echo', 'user', 'test', 'created!'])

if __name__ == '__main__':
    unittest.main()