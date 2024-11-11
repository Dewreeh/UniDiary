import Header from './Header'
import FormUser from './FormUser'

import AdminLogin from './AdminLogin'
import  './index.css';
import { BrowserRouter as Routes, Route, Link } from 'react-router-dom';

function App() {
  return (
        <div className="App">
          <div className="body">
            <Header />
            <div className="login-form">
              <FormUser />
            </div>
          </div>
          <Link to='/login_admin'><a className="AdminEntryButton">Вход для администратора</a></Link>
          
          <Routes>
              <Route path="/login_user" element={<App />} />
              <Route path="/login_admin" element={<AdminLogin />} />
          </Routes>
        </div>

  );
}

export default App;
