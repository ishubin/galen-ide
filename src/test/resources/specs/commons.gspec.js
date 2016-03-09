


this.commaSeparatedTextToJSON = function (text) {
    return JSON.stringify(text.split(',').map(function (t) {
        return t.trim();
    }));
};
