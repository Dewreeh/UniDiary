import Header from './components/Header'
import FormUser from './components/FormUser'
import LeftMenuButton from './components/LeftMenuButton'
import AdminLogin from './AdminLogin'
import  './components/index.css';
import {Routes, Route, Link } from 'react-router-dom';

function AdminPage() {
  return (
    <div className="App">
      <Header />
      <LeftMenuButton text='Студент'/>

    </div>

  );
}

export default AdminPage;