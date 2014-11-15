;
; Name: Ben Haines
; ID: bmh5wx
; Date: 10/31/14
; Filename: mathlib.s
;
	
	global collatz

	section .text

;
; collatz
; Parameter 1  - n
; Return value - steps to reach 1
;
; 
; Optimizations:
;   Doesn't back up ebp, offsets from esp
;   Uses bitshifts for multiplication/division when possible
;   Uses test to determine if a number is even efficiently
;   Uses lea to combine multiplication and addition into one step
;   Attempts to minimize jumps by ordering code in thoughtful way and by repeating code
;   There is no dead code
;   Accesses memory only when it has to, i.e. reading and pushing arguments


collatz:
	mov   ecx, [esp+4]	; Put n in ecx
    cmp   ecx, 1
    je    base_done
    test  ecx, 1
    je    even_case

odd_case:
    lea ecx, [2*ecx+ecx+1]         ; Multiply by 3 and add 1
    jmp recurs_call
    
even_case:
    shr ecx, 1

recurs_call:
    push  ecx           ; push args to stack
    call  collatz        ; recursive call
    add   eax, 1        ; takes steps = 1 + collatz(n2);
    add   esp, 4           ; remove parameters
    ret

base_done:
    mov eax, 0
	ret			; Return to the caller.
