import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { request } from '../../api/api';
import '../index.css';

function FormUser() {
  const [selectedRole, setSelectedRole] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const navigate = useNavigate();

  const handleRoleChange = (e) => {
    setSelectedRole(e.target.value);
  };

  const handleLogin = async (e) => {
    e.preventDefault();

    if (!selectedRole) {
      setError('Выберите роль');
      return;
    }

    try {
      const response = await request('/api/auth/login', 'POST', { 
        email, 
        password, 
        role: selectedRole 
      });

      localStorage.setItem('accessToken', response.accessToken);
      localStorage.setItem('userRole', response.role);
      localStorage.setItem('userId', response.userId);
      
      navigate(`/${selectedRole.toLowerCase()}`);
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
      </div>

      <div className="login-vertical-elems">
        {['STUDENT', 'LECTURER', 'DEAN_STAFF'].map((role) => (
          <label key={role} className='user-label'>
            <input
              type="radio"
              name="role"
              value={role}
              onChange={handleRoleChange}
            />
            {role === 'STUDENT' ? 'Я студент' :
             role === 'LECTURER' ? 'Я преподаватель' :
             'Я сотрудник'}
          </label>
        ))}
      </div>

      {error && <p className="info-message">{error}</p>}

      <button className="entry-button" onClick={handleLogin}>ВОЙТИ</button>
    </div>
  );
}

export default FormUser;
