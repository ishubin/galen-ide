CodeMirror.defineMode("galenspecs", function() {
return {
    startState: function() {return {inString: false};},
    token: function(stream, state) {
        // If a string starts here
        if (!state.inString && stream.peek() == '"') {
            stream.next();            // Skip quote
            state.inString = true;    // Update state
        }

        if (state.inString) {
            if (stream.skipTo('"')) { // Quote found on this line
                stream.next();          // Skip quote
                state.inString = false; // Clear flag
            } else {
                stream.skipToEnd();    // Rest of line is string
            }
            return "string";          // Token style
        } else {
            stream.skipTo('"') || stream.skipToEnd();
            return null;              // Unstyled token
        }
    }
};
});