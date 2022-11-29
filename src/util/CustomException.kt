package com.example.util

import java.sql.SQLException

class CustomException(var customMessage: String) : SQLException() {}
