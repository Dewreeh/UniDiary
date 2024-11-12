import Header from './Header'
import FormAdmin from './FormAdmin'
import App from './App'
import {Link, Routes, Route} from 'react-router-dom';

function AdminLogin() {
  return (
        <div className="App">
          <div className="body">
            <Header />
            <div className="login-form">
            <FormAdmin />
            </div>
          </div>
          <Link to='/'><a className="AdminEntryButton">Вход для остальных пользователей</a></Link>

        </div>

  );
}

export default AdminLogin;
