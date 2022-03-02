import React, { useState, useEffect } from "react";
import axios from "axios";

const Login = (props) => {
    const url = "/api";
    const { handleLoggedInUsername, handleActivePage } = props;
    const [ loginFailure, setLoginFailure ] = useState(false);
    const [ noUsername, setNoUsername ] = useState(false);
    const [ noPass, setNoPass ] = useState(false);
    const [credentials, setCredentials] = useState({
      username: "",
      password: "",
    });

    function getCookie() {
      const name = "user=";
      const decodedCookie = decodeURIComponent(document.cookie);
      const ca = decodedCookie.split(';');
      for(let i = 0; i <ca.length; i++) {
          let c = ca[i];
          while (c.charAt(0) === ' ') {
              c = c.substring(1);
          }
          if (c.indexOf(name) === 0) {
              return c.substring(name.length, c.length);
          }
      }
      return "";
  }

    useEffect(() => {
      var user = getCookie();
      if(user.length > 0) {
        handleLoggedInUsername(user);
        handleActivePage("/calendar");
      }
    }, [handleActivePage, handleLoggedInUsername])

    const setCookie = (user) => {
      const d = new Date();
      d.setTime(d.getTime() + (24*60*60*1000));
      const expires = "expires="+ d.toUTCString();
      document.cookie = "user=" + user + ";" + expires + ";path=/";
  }

    function submitForm() {
      if(credentials.username.length === 0) {
        setNoUsername(true);
        document.getElementById("username").classList.add("border-red-700")
        setNoPass(false);
        document.getElementById("password").classList.remove("border-red-700")
      } else if(credentials.password.length === 0) {
        setNoUsername(false);
        document.getElementById("username").classList.remove("border-red-700")
        setNoPass(true);
        document.getElementById("password").classList.add("border-red-700")
      } else {
        setNoUsername(false);
        setNoPass(false);
        document.getElementById("username").classList.remove("border-red-700")
        document.getElementById("password").classList.remove("border-red-700")

        axios
          .post(url + "/user/authenticate", credentials)
          .then(() => {
            setCookie(credentials.username);
            handleLoggedInUsername(credentials.username);
            handleActivePage("/calendar");
          })
          .catch(() => {
            setLoginFailure(true);
            document.getElementById("username").classList.add("border-red-700");
            document.getElementById("password").classList.add("border-red-700");
            document.getElementById("password").classList.remove("mb-3");
          });
        }
      }
    
      return (
        

        <div className="h-screen flex flex-col justify-center">
          <div className="mx-auto w-11/12 max-w-md">
            <h1 className="text-black text-xl font-sans text-center mb-3">
              Log In
            </h1>
            <form
              onSubmit={(e) => {
                e.preventDefault();
                submitForm();
              }}
            >
              <div className="bg-white rounded px-8 pt-6 pb-8 mb-4 flex flex-col">
                <div className="mb-4">
                  <label
                    className="block text-grey-darker text-sm font-bold mb-2 text-left"
                    htmlFor="username"
                  >
                    Username
                  </label>
                  <input
                    className="shadow appearance-none border rounded w-full py-2 px-3 text-grey-darker"
                    id="username"
                    type="text"
                    value={credentials.username}
                    onChange={(e) =>
                      setCredentials({
                        username: e.target.value,
                        password: credentials.password,
                      })
                    }
                  />
                  { loginFailure &&
                    <span class="text-xs text-red-700" id="passwordHelp">Incorrect Username or Password</span>
                  }
                  { noUsername &&
                    <span class="text-xs text-red-700" id="passwordHelp">Please enter a username</span>
                  }
                </div>
                <div className="mb-6">
                  <label
                    className="block text-grey-darker text-sm font-bold mb-2 text-left"
                    htmlFor="password"
                  >
                    Password
                  </label>
                  <input
                    className="shadow appearance-none border border-red rounded w-full py-2 px-3 text-grey-darker mb-3"
                    id="password"
                    type="password"
                    value={credentials.password}
                    onChange={(e) =>
                      setCredentials({
                        username: credentials.username,
                        password: e.target.value,
                      })
                    }
                  />
                  { loginFailure &&
                    <span class="text-xs text-red-700" id="passwordHelp">Incorrect Username or Password</span>
                  }
                  { noPass &&
                    <span class="text-xs text-red-700" id="passwordHelp">Please enter a password</span>
                  }
                </div>
                <div className="grid grid-cols-1 gap-4">
                  <button
                    className="p-2 pl-5 pr-5 bg-blue-500 text-white text-lg rounded-lg focus:border-4 border-blue-300 w-full"
                    type="submit" id="sign_in_button"
                    onClick={() => submitForm()}
                  >
                    <b>Sign In</b>
                  </button>
                  <div className="flex justify-center">
                    <p className="mx-1">{`Don't have an account?`}</p>
                    <div onClick={() => handleActivePage("/register")}
                      className="no-underline hover:underline text-blue-500 text-l mx-1 cursor-pointer"
                    >
                      <b>Sign up</b>
                    </div>
                  </div>
                </div>
              </div>
            </form>
          </div>
        </div>
      );
}

export default Login;