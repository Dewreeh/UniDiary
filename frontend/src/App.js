import Header from './components/Header'
import FormUser from './components/Login/FormUser'
import AdminLogin from './components/Login/AdminLogin'
import AdminPage from './components/Admin/AdminPage'
import  './components/index.css';
import {Routes, Route, Link } from 'react-router-dom';
import DeanStaffPage from './components/DeanStaff/DeanStaffPage';
import HeadmanPage from './components/Headman/HeadmanPage';
import StudensTable from './components/DeanStaff/StudentsTable';
import GroupStudentsTable from './components/DeanStaff/GroupStudentsPage';
import LecturerPage from './components/Lecturer/LecturerPage';
import AttendanceMarking from './components/General/AttendanceMarking';

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
        <Route path="/admin/*" element={<AdminPage />} />
        <Route path="/dean_staff/*" element={<DeanStaffPage />} />
        <Route path="/headman/*" element={<HeadmanPage />} />
        <Route path="/lecturer/*" element={<LecturerPage />} />

        <Route path="dean_staff/workflow/gruppy/group/:groupId" element={<GroupStudentsTable />} />
        <Route path="headman/workflow/poseshchaemost/schedule/:scheduleId/:date" element={<AttendanceMarking />}/>
        <Route path="lecturer/workflow/poseshchaemost/schedule/:scheduleId/:date" element={<AttendanceMarking />}/>
      </Routes>
    </div>

  );
}

export default App;
