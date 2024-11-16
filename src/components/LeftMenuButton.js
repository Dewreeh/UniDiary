import React from 'react';
import './index.css'
function LeftMenuButton(props){
    return (
        
        <button className="left-button-menu">
            <p class="text-button kirang-font">{props.text}</p>
        </button>

    );
}

export default LeftMenuButton;