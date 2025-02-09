import React from 'react';
import './index.css';
import LeftMenuButton from './LeftMenuButton';
import { Link } from 'react-router-dom';
import { transliterate } from 'transliteration'; 

function LeftNavBar(props) {
  const data = props.data;

  return (
    <nav className='navbar navbar-expand-lg'>
      <button
        className="navbar-toggler"
        type="button"
        data-toggle="collapse"
        data-target="#navbarCollapse"
        aria-controls="navbarCollapse"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
      </button>
      <div className="collapse navbar-collapse" id="navbarCollapse">
        <ul className="navbar-nav mr-auto sidenav">
          {data.map(item => (
            <li className="nav-item active" key={item}>
                <LeftMenuButton text={item} />
            </li>
          ))}
        </ul>
      </div>
    </nav>
  );
}

export default LeftNavBar;