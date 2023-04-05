
const elements = document.querySelectorAll('[data-off-time]');

if (elements) {
  let now = new Date();
  // now.setHours(0, 0, 0, 0);

  elements.forEach(element => {
    let componentOffTimeAttr = element.getAttribute('data-off-time');
    let componentOffTime = new Date(componentOffTimeAttr);
    // componentOffTime.setHours(0, 0, 0, 0);
    console.log(now, componentOffTime);
    if (componentOffTime <= now) {
         console.log("%s %s %s", "color: red", now, componentOffTime, "Hide:"+ true);
   		 element.remove();
    }else{
		console.log(now, componentOffTime, "hide:"+ false);
    }
  });
}