import React from 'react';
import { Link } from 'react-router-dom';
import './index.css';
import { transliterate } from 'transliteration'; 

function LeftMenuButton(props) {
  const linkText = transliterate(props.text).toLowerCase().replace(/\s+/g, '-');  
  const role = props.role;
  return (
    <Link to={`/${role}/workflow/${linkText}`} className="left-button-menu">
      <p className="text-button kirang-font">{props.text}</p>
    </Link>
  );
}

export default LeftMenuButton;
