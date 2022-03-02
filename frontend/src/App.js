import './App.css';
import Login from "./components/Login.js"
import CreateAccount from './components/CreateAccount.js';
import Calendar from './components/Calendar';
import EventForm from './components/EventForm';
import InviteeForm from './components/InviteeForm';
import React, { useState, useEffect } from "react";
import Footer from './components/Footer.js'
import Navbar from './components/NavBar';

function App() {

  const [ loggedInUsername, handleLoggedInUsername ] = useState("");
  const [activePage, handleActivePage] = useState("/");
  const [ selectedEvent, setSelectedEvent ] = useState([]);
  
  useEffect(() => {
    handleLoggedInUsername("");
    handleActivePage("/");
  }, []);

  useEffect(() => {
    switch (activePage) {
      case "/":
        if (loggedInUsername === "") {
          handleActivePage("/login");
        } else {
          handleActivePage("/calendar");
        }
        break;
      case "/login":
        if (loggedInUsername !== "") {
          handleActivePage("/home");
        }
        break;
      case "/calendar":
        if (loggedInUsername === "") {
          handleActivePage("/login");
        }
        break;
      case "/register":
        if (loggedInUsername !== "") {
          handleActivePage("/calendar");
        }
        break;
      case "/eventform":
        if (loggedInUsername !== "") {
          handleActivePage("/eventform");
        }
        break;
      case "/inviteeform":
        if (loggedInUsername !== "") {
          handleActivePage("/inviteeform");
        }
        break;
      default: 
        if (loggedInUsername === "") {
          handleActivePage("/login");
        } else {
          handleActivePage("/calendar");
        }
        break;
    }
  }, [activePage, loggedInUsername]);

  return (
    <>
      <div className="h-screen w-full flex flex-col">
        <Navbar loggedInUsername={loggedInUsername} handleLoggedInUsername={handleLoggedInUsername} handleActivePage={handleActivePage} />
        {activePage === "/" ? loggedInUsername === "" ? (
          <Login
          handleActivePage={handleActivePage}
          handleLoggedInUsername={handleLoggedInUsername}
        />
         ) : (
          <>
          <Calendar 
            loggedInUsername={loggedInUsername} 
            handleLoggedInUsername={handleLoggedInUsername} 
            setSelectedEvent={setSelectedEvent}
            />
        </>
         ) : null}
        {activePage === "/login" ? (
          loggedInUsername !== "" ? (
            <>
              <Calendar 
                loggedInUsername={loggedInUsername} 
                handleLoggedInUsername={handleLoggedInUsername} 
                setSelectedEvent={setSelectedEvent}
                />
            </>
          ) : (
            <Login
              handleActivePage={handleActivePage}
              handleLoggedInUsername={handleLoggedInUsername}
            />
          )
        ) : null}
        {activePage === "/calendar" ? (
          loggedInUsername === "" ? (
            <Login
              handleActivePage={handleActivePage}
              handleLoggedInUsername={handleLoggedInUsername}
            />
          ) : (
            <>
              <Calendar 
                loggedInUsername={loggedInUsername} 
                handleLoggedInUsername={handleLoggedInUsername} 
                handleActivePage={handleActivePage}
                setSelectedEvent={setSelectedEvent}
                />
            </>
          )
        ) : null}
        {activePage === "/register" ? (
          loggedInUsername !== "" ? (
            <>
              <Calendar 
                loggedInUsername={loggedInUsername} 
                handleLoggedInUsername={handleLoggedInUsername} 
                setSelectedEvent={setSelectedEvent}
                />
            </>
          ) : (
            <CreateAccount handleActivePage={handleActivePage} />
          )
        ) : null}
        {activePage === "/eventform" ? (
          loggedInUsername === "" ? (
            <Login
              handleActivePage={handleActivePage}
              handleLoggedInUsername={handleLoggedInUsername}
            />
          ) : (
            <EventForm loggedInUsername={loggedInUsername} handleActivePage={handleActivePage} />
          )
        ) : null}
        {activePage === "/inviteeform" ? (
          loggedInUsername === "" ? (
            <Login
              handleActivePage={handleActivePage}
              handleLoggedInUsername={handleLoggedInUsername}
            />
          ) : (
            <InviteeForm loggedInUsername={loggedInUsername} 
					handleActivePage={handleActivePage} selectedEvent={selectedEvent}/>
          )
        ) : null}
        { activePage !== "/inviteeform" &&
        	<Footer />
    	}
      </div>
    </>
  );
}

export default App;