import React from 'react';
import './index.css'
import LeftMenuButton from './LeftMenuButton';
function LeftNavBar(props){
    var data = props.data;
    /*return (
        <div className='left-nav-bar'>
            {data.map(item =>
                <LeftMenuButton text={item} />
            )}
        </div>

    );
    */

    return (
        <nav className='navbar navbar-expand-lg'>

            <button
                class="navbar-toggler"
                type="button"
                data-toggle="collapse"
                 data-target="#navbarCollapse"
                aria-controls="navbarCollapse"
                aria-expanded="false"
                aria-label="Toggle navigation"
            >
            <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarCollapse">
                <ul class="navbar-nav mr-auto sidenav">
                    {data.map(item =>
                        <li class="nav-item active">
                            <a class="nav-link"><LeftMenuButton text={item} /></a>
                        </li>
                    )}
                </ul>
            </div>
        </nav>

    );

}

export default LeftNavBar;