import React from 'react';
import './index.css'
function FormUser(){
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
            type="text"
            placeholder="Пароль"
            id="inputPasword"
            />
        </div>
        <div className="login-vertical-elems">
            <label className='user-label'>
                 <input type="radio" name="options" value="option1"/>Я студент
            </label>
            <label className='user-label'>
                <input type="radio" name="options" value="option2" />Я преподаватель
            </label>
            <label className='user-label'>
                <input  type="radio" name="options" value="option3"/>Я сотрудник
            </label>
        </div>
        <button className="entry-button">Войти</button>
    </div>

    );
}

export default FormUser;