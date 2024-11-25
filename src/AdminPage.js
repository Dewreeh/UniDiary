import Header from './components/Header'
import FormUser from './components/FormUser'
import AdminLogin from './AdminLogin'
import  './components/index.css';
import {Routes, Route, Link } from 'react-router-dom';
import WorkFlow from './components/WorkFlow';
import LeftNavBar from './components/LeftNavBar';

function AdminPage() {
  return (
    <div className="AdminPage">

            <div className="workflow-container">
              <LeftNavBar data={["Факультеты", "Сотрудники деканатов", "Статистика"]}/>
              <WorkFlow title="Привет"/>
            </div>
            
    </div>
    

  );
}

export default AdminPage;