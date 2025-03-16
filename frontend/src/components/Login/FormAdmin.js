import React from 'react';
import '../index.css'
import { Route, Routes, Link, Router } from 'react-router-dom';
import AdminPage from '../Admin/AdminPage';

function FormAdmin(){
    return(
            <div className="form-user">
                <div className="login-horisontal-elems">
                    <h1 className="form-title">Вход в систему</h1>
                    <input
                        className="input-item"
                        type="text"
                        placeholder="Почта"
                        id="inputEmail"
                    />
                    <input
                    className="input-item"
                    type="password"
                    placeholder="Пароль"
                    id="inputPasword"
                    />
                </div>
                <Link to="/admin">
                <button className="entry-button">Войти</button>
                </Link>

            </div>
            
        );
}

export default FormAdmin;