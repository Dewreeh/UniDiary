import React from 'react';
import { Routes, Route } from 'react-router-dom';
import LeftNavBar from '../LeftNavBar';
import WorkFlow from '../WorkFlow';
import AdminLogin from '../Login/AdminLogin';

function HeadmanPage() {
  const menuData = ["Группы", "Посещаемость", "Расписание"];
  const role = "lecturer";
  
  return (
    <div className="DeanStaffPage">
      <div className="workflow-container">
        <LeftNavBar data={menuData} role={role} />
        <div className="content-area">
          <Routes>
            <Route path="workflow/:section" element={<WorkFlow />} />
          </Routes>
        </div>
      </div>
    </div>
  );
}

export default HeadmanPage;