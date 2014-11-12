;
; Name: Ben Haines
; ID: bmh5wx
; Date: 10/31/14
; Filename: mathlib.s
;
	
	global product

	section .text

;
; product
; Parameter 1  - a
; Parameter 2  - b
; Return value - a + b
;

product:
	; Standard prologue
	push  ebp		; Save the old base pointer
	mov   ebp, esp		; Set new value of the base pointer
	push  esi		; Save registers
    push  ecx

	xor   eax, eax		; Place zero in EAX.

	mov   esi, [ebp+8]	; Put a in esi
	mov   ecx, [ebp+12]	; Put b in ecx

	jmp   product_loop	


product_loop:
	add   eax, esi		; Add a to total

	dec   ecx		; Decrement ECX, the counter of how many
				; left to do.
	cmp   ecx, 0		; If there are more than zero elements
	jg    product_loop	; left to add up, then do the loop again.

product_done:
	; At this point, the loop is done, and we have the sum of the
	; vector elements in EAX, which is exactly where we want the
	; return value to be.

	; Standard epilogue
    pop   ecx
	pop   esi		; Restore registers that we used.
				; Note - no local variables to dealocate.
	pop   ebp		; Restore the caller's base pointer.
	ret			; Return to the caller.


	global power

	section .text

;
; product
; Parameter 1  - base
; Parameter 2  - exponent
; Return value - a ^ b
;

power:
	; Standard prologue
	push  ebp		; Save the old base pointer
	mov   ebp, esp		; Set new value of the base pointer
	push  esi		; Save registers

	xor   eax, eax		; Place zero in eax.
	xor   esi, esi		; Place one in esi, to be used for holding partial results
    add   esi, 1
	mov   ecx, [ebp+8]	; Put base in ecx
	mov   edx, [ebp+12]	; Put exponent in edx

	jmp   power_body	

power_body:
    cmp   edx, 1
    je    base_done
    dec   edx;
    push  edx           ; push args to stack
    push  ecx
    call  power        ; recursive call
	mov   esi, eax      ; move result into esi
    pop   eax           ; remove parameters
    pop   eax
    push  esi           ;push args to stack
    push  ecx
    call  product       ; base * base^(exponent - 1)
    mov esi, eax        ; move result into esi
    pop eax             ; remove args from stack
    pop eax             
    jmp   other_done

base_done:
    mov eax, ecx
	; Standard epilogue
	pop   esi		; Restore registers that we used.
				; Note - no local variables to dealocate.
	pop   ebp		; Restore the caller's base pointer.
	ret			; Return to the caller.

other_done:
    mov eax, esi
	; Standard epilogue
	pop   esi		; Restore registers that we used.
				; Note - no local variables to dealocate.
	pop   ebp		; Restore the caller's base pointer.
	ret			; Return to the caller.
