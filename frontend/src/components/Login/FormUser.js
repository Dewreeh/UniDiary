import React from 'react';
import '../index.css'
function FormUser(){
    return(
    <div className="form-user">
        <div className="login-horisontal-elems">
            <h1 className="form-title">Вход в систему</h1>
            <input className="input-item" type="text" placeholder="Почта" id="inputEmail"/>
            <input className="input-item" type="password" placeholder="Пароль" id="inputPasword"/>
        </div>
        <div className="login-vertical-elems">
            <label className='user-label'>
                 <input type="radio" name="options" value="STUDENT"/>Я студент
            </label>
            <label className='user-label'>
                <input type="radio" name="options" value="LECTURER" />Я преподаватель
            </label>
            <label className='user-label'>
                <input  type="radio" name="options" value="WORKER"/>Я сотрудник
            </label>
        </div>
        <button className="entry-button">ВОЙТИ</button>
    </div>

    );
}

export default FormUser;