import Header from './Header'
import FormAdmin from './FormAdmin'
import App from './App'
import {BrowserRouter as Link, Routes, Route} from 'react-router-dom';

function AdminLogin() {
  return (
        <div className="App">
          <div className="body">
            <Header />
            <div className="login-form">
            <FormAdmin />
            </div>
          </div>
          <Link to='/login_user'><a className="AdminEntryButton">Вход для остальных пользователей</a></Link>

          <Routes>
            <Route path='/login_user' component={<App />} /> 
          </Routes>
        </div>

  );
}

export default AdminLogin;
