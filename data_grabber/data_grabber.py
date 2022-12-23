from dotenv import dotenv_values


class DataGrabber:
    def __init__(self, password1):
        self._config = dotenv_values(".env")
        self.base_url = self._config["BASE_URL"]
        self.username1 = self._config["USERNAME1"]
        self.password1 = password1
        self.username2 = self._config["USERNAME2"]
        self.password2 = self._config["PASSWORD2"]
        

    def get_config(self, key):
        return self._config[key]