import React from 'react';
import { Routes, Route } from 'react-router-dom';
import LeftNavBar from '../LeftNavBar';
import WorkFlow from '../WorkFlow';
import AdminLogin from '../Login/AdminLogin';

function DeanStaffPage() {
  const menuData = ["Группы", "Студенты", "Расписание", "Старосты", "Дисциплины"];
  const role = "dean_staff"
  return (
    <div className="DeanStaffPage">
      <div className="workflow-container">
        <LeftNavBar data={menuData} role={role} />
        <Routes>
          <Route path="workflow/:section" element={<WorkFlow />} />
        </Routes>
      </div>
    </div>
  );
}

export default DeanStaffPage;