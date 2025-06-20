import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { request } from '../../api/api'; 
import '../index.css';

function FormAdmin() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate(); 

    const handleLogin = async () => {
        try {
            const response = await request('/api/auth/login', 'POST', { 
                email, 
                password, 
                role: "ADMIN" 
            }, false);
    
    
            localStorage.setItem('accessToken', response.access_token);
            localStorage.setItem('userRole', response.role[0].authority); 
            navigate('/admin');
        } catch (err) {
            setError('Ошибка входа. Проверьте почту и пароль.');
        }
    };
    
    return (
        <div className="form-user">
            <div className="login-horisontal-elems">
                <h1 className="form-title">Вход в систему</h1>
                <input
                    className="input-item"
                    type="text"
                    placeholder="Почта"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
                <input
                    className="input-item"
                    type="password"
                    placeholder="Пароль"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                {error && <p className="info-message">{error}</p>}
            </div>
            <button className="entry-button" onClick={handleLogin}>Войти</button>
        </div>
    );
}

export default FormAdmin;
