import Header from './components/Header'
import FormUser from './components/FormUser'
import AdminLogin from './AdminLogin'
import  './components/index.css';
import {Routes, Route, Link } from 'react-router-dom';

function App() {
  return (
    <div className="App">
      <Header />
      <Routes>
        <Route 
          path="/" 
          element={
            <div className="body">
              <div className="login-form">
              <FormUser />
              </div>
              <Link to="/login_admin">
                <a className="change-type-of-user">Вход для администратора</a>
              </Link>
            </div>
          } 
        />
        
        <Route path="/login_admin" element={<AdminLogin />} />
      </Routes>
    </div>

  );
}

export default App;
