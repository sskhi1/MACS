
    @KBD
    D = A
    @end
    M = D
(LOOP)
    @currcolor
    M = 0
	(start)
    @SCREEN
    D = A
    @i
    M = D
    @KBD
    D = M
    @BLACK_CHANGE
    D;JNE
    @COLOR
    0;JMP
(BLACK_CHANGE)
    @currcolor
    M = -1
(COLOR)
    @i
    D = M
    @end
    D = M - D
    @LOOP
    D;JEQ
    // color i-th row
    @currcolor
    D = M
    @i
    A = M
    M = D
    // i += 1
    @i
    M = M + 1
    @COLOR
    0;JMP
