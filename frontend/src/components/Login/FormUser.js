import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import '../index.css';

function FormUser() {
  const [selectedRole, setSelectedRole] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleRoleChange = (e) => {
    setSelectedRole(e.target.value);
  };

  const handleLogin = () => {
    if (!selectedRole) {
      alert('Выберите роль');
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
          id="inputEmail"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <input
          className="input-item"
          type="password"
          placeholder="Пароль"
          id="inputPasword"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
      </div>

      <div className="login-vertical-elems">
        <label className='user-label'>
          <input
            type="radio"
            name="options"
            value="STUDENT"
            onChange={handleRoleChange}
          />
          Я студент
        </label>
        <label className='user-label'>
          <input
            type="radio"
            name="options"
            value="LECTURER"
            onChange={handleRoleChange}
          />
          Я преподаватель
        </label>
        <label className='user-label'>
          <input
            type="radio"
            name="options"
            value="DEAN_STAFF"
            onChange={handleRoleChange}
          />
          Я сотрудник
        </label>
      </div>
        <Link to={`/${selectedRole.toLowerCase()}`} onClick={handleLogin}>
          <button className="entry-button">ВОЙТИ</button>
        </Link>
    
    </div>
  );
}

export default FormUser;
