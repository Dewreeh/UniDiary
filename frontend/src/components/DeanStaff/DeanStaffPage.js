import React from 'react';
import { Routes, Route } from 'react-router-dom';
import LeftNavBar from '../LeftNavBar';
import WorkFlow from '../WorkFlow';
import AdminLogin from '../Login/AdminLogin';

function DeanStaffPage() {
  const menuData = ["Группы", "Студенты", "Старосты"];

  return (
    <div className="AdminPage">
      <div className="workflow-container">
        <LeftNavBar data={menuData} />
        <Routes>
          <Route path="workflow/:section" element={<WorkFlow />} />
          <Route path="/login_admin" element={<AdminLogin />} />
        </Routes>
      </div>
    </div>
  );
}

export default DeanStaffPage;