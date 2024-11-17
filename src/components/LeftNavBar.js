import React from 'react';
import './index.css'
import LeftMenuButton from './LeftMenuButton';
function LeftNavBar(props){
    var data = props.data;
    return (
        <div className='left-nav-bar'>
            {data.map(item =>
                <LeftMenuButton text={item} />
            )}
        </div>

    );
}

export default LeftNavBar;