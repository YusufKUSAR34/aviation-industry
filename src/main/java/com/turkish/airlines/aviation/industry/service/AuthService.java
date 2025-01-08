package com.turkish.airlines.aviation.industry.service;

import com.turkish.airlines.aviation.industry.request.LoginRequest;
import com.turkish.airlines.aviation.industry.request.RegisterRequest;
import com.turkish.airlines.aviation.industry.response.AuthResponse;
import com.turkish.airlines.aviation.industry.response.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
} 