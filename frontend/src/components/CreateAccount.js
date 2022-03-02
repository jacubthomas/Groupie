import React, { useState } from "react";
import axios from "axios";

const CreateAccount = (props) => {
    const url = "/api";

    const { handleActivePage } = props;
    const [ usernameTaken, setUsernameTaken ] = useState(false);
    const [ passwordMatch, setPasswordMatch ] = useState(false);
    const [ noUsername, setNoUsername ] = useState(false);
    const [ noPass, setNoPass ] = useState(false);
    const [ noConfirm, setNoConfirm ] = useState(false);

    const [credentials, setCredentials] = useState({
        username: "",
        password: "",
        confirmPassword: ""
      });

    function usernameAlreadyExists() {
      axios
        .get(url + "/user/checkUsername/" + credentials.username)
        .then((res) => {
          console.log(res);
          if(res.data === false) {
            setUsernameTaken(false);
            document.getElementById("username").classList.remove("border-red-700")
          } else {
            setUsernameTaken(true);
            document.getElementById("username").classList.add("border-red-700")
          }
        })
        .catch(() => {
          
          
        });
    }

    function passwordsMatch() {
      axios
        .post(url + "/user/checkMatchingPasswords", {
          username: "",
          password: credentials.password,
          confirmPassword: credentials.confirmPassword,
        })
        .then(() => {
          setPasswordMatch(false);
          document.getElementById("password").classList.remove("border-red-700")
          document.getElementById("confirm-password").classList.remove("border-red-700")
          document.getElementById("password").classList.add("mb-3")
          document.getElementById("confirm-password").classList.add("mb-3")
          return true;
        })
        .catch(() => {
          // TODO set error messages visibility, color
          setPasswordMatch(true);
          setNoConfirm(false);
          setNoPass(false);
          document.getElementById("password").classList.add("border-red-700")
          document.getElementById("confirm-password").classList.add("border-red-700")
          document.getElementById("password").classList.remove("mb-3")
          document.getElementById("confirm-password").classList.remove("mb-3")
          return false;
        });
      return false;
    }

    function submitForm() {
      if(credentials.username.length === 0) {
        setNoUsername(true);
        document.getElementById("username").classList.add("border-red-700")
      } else if(credentials.password.length === 0) {
        setNoPass(true);
        document.getElementById("password").classList.add("border-red-700")
        setNoUsername(false);
        document.getElementById("username").classList.remove("border-red-700")
      } else if(credentials.confirmPassword.length === 0) {
        setNoConfirm(true);
        document.getElementById("confirm-password").classList.add("border-red-700")
        setNoPass(false);
        document.getElementById("password").classList.remove("border-red-700")
        setNoUsername(false);
        document.getElementById("username").classList.remove("border-red-700")
      } else {

        // send credentials to back end (ensure connection is encrypted)

        document.getElementById("username").classList.remove("border-red-700")
        document.getElementById("password").classList.remove("border-red-700")
        document.getElementById("confirm-password").classList.remove("border-red-700")
        setNoUsername(false);
        setNoPass(false);
        setNoConfirm(false);

        axios
          .post(url + "/user/create", {
            username: credentials.username,
            password: credentials.password,
            confirmPassword: credentials.confirmPassword,
          })
          .then(() => {
            handleActivePage("/calendar");
          })
          .catch(() => {
            // TODO set error messages visibility, color
          });
      }
    }
    return (
      <div className="h-screen flex flex-col justify-center">
        <div className="mx-auto w-11/12 max-w-md">
          <h1 className="text-black text-xl font-sans text-center">
            Sign Up
          </h1>
          <form onSubmit={(e) => {
            e.preventDefault();
            e.stopPropagation();
            submitForm();
          }}>
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
                  onChange={(e) => setCredentials({
                    username: e.target.value,
                    password: credentials.password,
                    confirmPassword: credentials.confirmPassword,
                  })}
  
                  onBlur={usernameAlreadyExists}
                />
                { usernameTaken && 
                  <span class="text-xs text-red-700" id="passwordHelp">Username is already taken</span>
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
                  onChange={(e) => setCredentials({
                    username: credentials.username,
                    password: e.target.value,
                    confirmPassword: credentials.confirmPassword,
                  })}
                />
                { passwordMatch && 
                  <span class="text-xs text-red-700" id="passwordHelp">Passwords do not match. </span>
                }
                { noPass &&
                  <span class="text-xs text-red-700" id="passwordHelp">Please enter a password</span>
                }
              </div>
              <div className="mb-6">
                <label
                  className="block text-grey-darker text-sm font-bold mb-2 text-left"
                  htmlFor="confirm-password"
                >
                  Confirm Password
                </label>
                <input
                  className="shadow appearance-none border border-red rounded w-full py-2 px-3 text-grey-darker mb-3"
                  id="confirm-password"
                  type="password"
                  value={credentials.confirmPassword}
                  onChange={(e) => setCredentials({
                    username: credentials.username,
                    password: credentials.password,
                    confirmPassword: e.target.value,
                  })}
                  onBlur={passwordsMatch}
                />
                { passwordMatch && 
                  <span class="text-xs text-red-700" id="passwordHelp">Passwords do not match. </span>
                }
                { noConfirm &&
                  <span class="text-xs text-red-700" id="passwordHelp">Please enter a confirmation password</span>
                }
              </div>
              <div className="grid grid-cols-1 gap-4">
                <button type="submit"
                  className="p-2 pl-5 pr-5 bg-blue-500 text-white text-lg rounded-lg focus:border-4 border-blue-300 w-full"
                >
                  <b>Create User</b>
                </button>
                <button
                  className="p-2 pl-5 pr-5 bg-gray-400 text-gray-100 text-lg rounded-lg focus:border-4 border-gray-300 w-full"
                  onClick={(e) => {
                    e.stopPropagation();
                    e.preventDefault();
                    handleActivePage("/login");
                  }}
                >
                  <b>Cancel</b>
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>
    );
}

export default CreateAccount;