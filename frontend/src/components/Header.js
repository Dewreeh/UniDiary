import React from 'react';
import './index.css';
import { useLocation, useNavigate } from 'react-router-dom';

function Header() {
    const navigate = useNavigate();
    const location = useLocation();

    const handleLogout = () => {
        localStorage.clear();
        navigate('/');
    };


    const hideLogout = ['/', '/login', '/register'].includes(location.pathname);

    return (
        <div className="header">
            <div className="title">UniDiary</div>
            {!hideLogout && (
                <button className="logout-button" onClick={handleLogout}>
                    Выйти
                </button>
            )}
        </div>
    );
}

export default Header;