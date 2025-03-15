import Header from '../Header'
import FormAdmin from './FormAdmin'
import App from '../../App'
import {Link, Routes, Route} from 'react-router-dom';
import '../index.css';

function AdminLogin() {
  return (
        <div className="AdminLogin">
          <div className="body">
            <div className="login-form">
            <FormAdmin />
            </div>
          </div>
          <Link to="/" className="change-type-of-user">Вход для остальных пользователей</Link>

        </div>

  );
}

export default AdminLogin;
