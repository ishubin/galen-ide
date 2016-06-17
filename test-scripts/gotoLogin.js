WelcomePage = $page("Welcome page", {
    loginButton: ".button-login"
});


var page = new WelcomePage(driver);
page.loginButton.click();
