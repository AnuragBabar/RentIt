import React, { createContext, useState, useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [token, setToken] = useState(localStorage.getItem('token'));
    const navigate = useNavigate();

    useEffect(() => {
        const savedRole = localStorage.getItem('role');
        const savedUserId = localStorage.getItem('userId');
        if (token && savedRole) {
            setUser({ role: savedRole, userId: savedUserId });
        }
    }, [token]);

    const login = (newToken, role, userId) => {
        console.log("AuthContext login called with:", { newToken, role, userId });
        localStorage.setItem('token', newToken);
        localStorage.setItem('role', role);
        localStorage.setItem('userId', userId);
        setToken(newToken);
        setUser({ role, userId });
        console.log("AuthContext state set. Redirecting based on role:", role);

        if (role === 'OWNER') {
            navigate('/owner-dashboard');
        } else {
            navigate('/renter-dashboard');
        }
    };

    const logout = () => {
        localStorage.clear();
        setToken(null);
        setUser(null);
        navigate('/login');
    };

    return (
        <AuthContext.Provider value={{ user, token, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
