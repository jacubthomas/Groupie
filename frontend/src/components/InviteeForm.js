import React, { useState } from "react";
//import axios from "axios";

const InviteeForm = (props) => {
	const { handleActivePage, selectedEvent/*, loggedInUsername*/ } = props;
	const [clicked, setClicked] = useState(0);
	const [activePage, setActivePage] = useState(true);
	const [available, setAvailable] = useState("No");
	const [excitement, setExcitement] = useState(0);

	function click(id) {
		if (clicked === id)
			setClicked(-1);
		else
			setClicked(id);

	}
	function closeSB() {
		setActivePage(false);
		handleActivePage("/calendar");
	}

	function unavailableAll() {
		setAvailable("No");
		setExcitement(0);
		//finalize();
	}
	/*function finalize()
	{
		let responseStatus = "false";
			preferences.map((p) => {
				return(
					p !== "Unavailable" &&
					(
						responseStatus = "true"
					)
				);
				})
				//
			  const proposalInfo = {
				proposer: {
				  username: loggedInUsername
				},
				invitees: inviteeArray,
				proposal_name: selectedEventName,
				events: [ selectedEvent ]
			  }
			  console.log(proposalInfo.events);
		
			  axios
				.post(url + "/user/proposer/createProposal", proposalInfo)
				.then((res) => {
				  console.log(res)
				})
				.catch((err) => {
				  console.log(err);
				})
	}*/
	return (
		<div>
			{ activePage &&
				<div className="fixed inset-0 overflow-hidden" aria-labelledby="slide-over-title" role="dialog" aria-modal="true">
					<div className="absolute inset-0 overflow-hidden">
						<div className="absolute inset-0 bg-gray-500 bg-opacity-75 transition-opacity" aria-hidden="true"></div>
						<div className="fixed inset-y-0 right-0 pl-10 max-w-full flex">
							<div className="w-screen max-w-md">
								<div className="h-full flex flex-col bg-white shadow-xl overflow-y-scroll">
									<div className="py-6 overflow-y-auto px-4 sm:px-6">
										<div className="flex items-start justify-between">
											<h2 className="text-lg font-medium text-gray-900 m-2" id="slide-over-title">
												<span className="text-4xl font-semibold">{selectedEvent.eventName}</span><br />
												<span className="text-gray-500 text-2xl font-medium">@ Venue</span><br />
												<span className="text-gray-500 text-2xl font-medium">{selectedEvent.startTime + " - " + selectedEvent.endTime}</span>
											</h2>
											<div className="ml-3 h-7 flex items-center"></div>
											<div className="ml-3 h-7 flex items-center">
												<button name="exit" type="button" className="m-4 text-gray-500 hover:text-red-600" onClick={() => closeSB()}>
													<span className="sr-only">Close panel</span>
													<svg className="h-6 w-6" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" aria-hidden="true">
														<path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12" />
													</svg>
												</button>
											</div>
										</div>
										<div className="my-auto py-4">
											<p className="text-xl text-gray-500 font-medium">Invited</p>
											<hr />
											<ul name="invited_members" className="m-2 text-gray-600 font-small text-center">
												{selectedEvent.invited.map((inv, index) => {
													return (
														<li key={inv + "_" + index}>{inv}</li>
													);
												})
												}
											</ul>
										</div>
										<div className="my-auto py-2">
											<p className="text-xl text-gray-500 font-medium">Response</p>
											<hr />
											<br />
											<div id="Availability" className="flex-col text-center w-100 mx-auto" onClick={() => click("Availability")}>
												<div className="grid justify-center">
													<button type="button" className="flex justify-center w-72 rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-sm font-medium 
								text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-gray-100 focus:ring-indigo-500"
														id="menu-button" aria-expanded="true" aria-haspopup="true">
														Availability
							      <svg className="-mr-1 ml-2 h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
															<path fillRule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clipRule="evenodd" />
														</svg>
													</button>
												</div>
												{clicked === "Availability" &&
													<ul id="menu" className="right-0 mx-auto w-72 justify-center rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 focus:outline-none"
														role="menu" aria-orientation="vertical" aria-labelledby="menu-button" tabIndex="-1">
														<li id="avail_1" key="a_1" className="text-gray-700 hover:bg-indigo-300 block px-4 py-2 text-sm 
											preference_menu" role="menuitem" tabIndex="-1" onClick={() => setAvailable("Yes")}>Yes</li>
														<li id="avail_2" key="a_2" className="text-gray-700 hover:bg-indigo-300 block px-4 py-2 text-sm 
											preference_menu" role="menuitem" tabIndex="-1" onClick={() => setAvailable("Maybe")}>Maybe</li>
														<li id="avail_2" key="a_3" className="text-gray-700 hover:bg-indigo-300 block px-4 py-2 text-sm 
											preference_menu" role="menuitem" tabIndex="-1" onClick={() => setAvailable("No")}>No</li>
													</ul>
												}
											</div>
											<div id="Excitement" className="flex-col text-center w-100 mx-auto" onClick={() => click("Excitement")}>
												<div className="grid justify-center">
													<button type="button" className="flex justify-center w-72 rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-sm font-medium 
								text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-gray-100 focus:ring-indigo-500"
														id="menu-button" aria-expanded="true" aria-haspopup="true">
														Excitement
							      <svg className="-mr-1 ml-2 h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
															<path fillRule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clipRule="evenodd" />
														</svg>
													</button>
												</div>
												{clicked === "Excitement" &&
													<ul id="menu" className="right-0 mx-auto w-72 justify-center rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 focus:outline-none"
														role="menu" aria-orientation="vertical" aria-labelledby="menu-button" tabIndex="-1">
														<li id="excite_0" key="e_0" className="text-gray-700 hover:bg-indigo-300 block px-4 py-2 text-sm 
											preference_menu" role="menuitem" tabIndex="-1" onClick={() => setExcitement(0)}>0</li>
														<li id="excite_1" key="e_1" className="text-gray-700 hover:bg-indigo-300 block px-4 py-2 text-sm 
											preference_menu" role="menuitem" tabIndex="-1" onClick={() => setExcitement(1)}>1</li>
														<li id="excite_2" key="e_2" className="text-gray-700 hover:bg-indigo-300 block px-4 py-2 text-sm 
											preference_menu" role="menuitem" tabIndex="-1" onClick={() => setExcitement(2)}>2</li>
														<li id="excite_3" key="e_3" className="text-gray-700 hover:bg-indigo-300 block px-4 py-2 text-sm 
											preference_menu" role="menuitem" tabIndex="-1" onClick={() => setExcitement(3)}>3</li>
														<li id="excite_4" key="e_4" className="text-gray-700 hover:bg-indigo-300 block px-4 py-2 text-sm 
											preference_menu" role="menuitem" tabIndex="-1" onClick={() => setExcitement(4)}>4</li>
														<li id="excite_5" key="e_5" className="text-gray-700 hover:bg-indigo-300 block px-4 py-2 text-sm 
											preference_menu" role="menuitem" tabIndex="-1" onClick={() => setExcitement(5)}>5</li>

													</ul>
												}
											</div>
										</div>
										<div className="my-auto py-2">
											<p className="text-xl text-gray-500 font-medium">Assigned</p>
											<hr />
											<div className="subtotal mx-auto text-md text-center p-2">
												<p>
													<span id="avail_assigned_label" className="text-indigo-700">Availability: </span><span id="avail_assigned">{available}</span>
													<br />
													<span id="excite_assigned_label" className="text-indigo-700">Excitement: </span><span id="excite_assigned">{excitement}</span>
												</p>
											</div>
										</div>
										<div className="my-auto py-2">
											<p className="text-xl text-gray-500 font-medium">Send it!</p>
											<hr />
											<div className="mt-6">
												<button className="flex mt-100 w-full justify-center items-center px-6 py-3 border border-transparent rounded-md
						 shadow-sm text-base font-medium text-white bg-indigo-600 hover:bg-indigo-700">Finalize</button>
											</div>
											<div className="mt-6">
												<button id="unavailable" className="flex mt-100 w-full justify-center items-center px-6 py-3 border border-transparent rounded-md
									 shadow-sm text-base font-medium text-white bg-red-600 hover:bg-red-700" onClick={() => unavailableAll()}>
													Unavailable
							</button>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			}
		</div>
	);
}
export default InviteeForm;
