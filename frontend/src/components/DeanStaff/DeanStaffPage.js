import React from 'react';
import { Routes, Route } from 'react-router-dom';
import LeftNavBar from '../LeftNavBar';
import WorkFlow from '../WorkFlow';
import AdminLogin from '../Login/AdminLogin';
import ScheduleTable from './SchedulePage';

function DeanStaffPage() {
  const menuData = ["Группы", "Студенты", "Преподаватели", "Расписание", "Старосты", "Дисциплины", "Отчёты"];
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