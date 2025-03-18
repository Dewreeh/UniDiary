import React, { useState } from 'react';
import './index.css';
import LeftMenuButton from './LeftMenuButton';

function LeftNavBar({ data, role }) {
  const [isOpen, setIsOpen] = useState(false);

  const toggleMenu = () => {
    setIsOpen(!isOpen);
  };

  return (
    <>
      <button className="menu-toggle" onClick={toggleMenu}>
        ☰
      </button>

      <nav className={`navbar navbar-expand-lg left-nav-bar ${isOpen ? 'open' : 'closed'}`}>
        <ul className="navbar-nav mr-auto sidenav">
          {data.map((item) => (
            <li className="nav-item active" key={item}>
              <LeftMenuButton text={item} role={role} />
            </li>
          ))}
        </ul>
      </nav>
    </>
  );
}

export default LeftNavBar;
