unit pd_console;

interface

implementation

uses
  unix;

  type
    textbuf = array[0..255] of char;
    TTextRec = packed record
      Handle: THandle;
      Mode: LongInt;
      BufSize: SizeInt;
      _private: SizeInt;
      BufPos,
      BufEnd: SizeInt ;
      BufPtr: ^textbuf;
      OpenFunc: Pointer;
      InOutFunc: Pointer;
      FlushFunc: Pointer;
      CloseFunc: Pointer;
      UserData: array[1..32] of Byte;
      name: array[0..255] of char;
      LineEnd: string[3];
      Buffer: array[0..255] of char;
    end;
    
    TFlushFunc = function(var Text: TTextRec): Integer;
    
  var
    OldFunc, OldFlushFunc: TFlushFunc;
    S: string;
  
  function NewWrite(var Text: TTextRec): Integer;
  begin 
    Result:= OldFunc(Text);  
    //OldFlushFunc(Text); 
    //FileWrite(Text.Handle, S[1], 4096);
  end;
  
  function NewFlush(var Text: TTextRec): Integer;
  begin 
    //if Text.Mode = fmOutput then
      OldFunc(Text);  
   //FileWrite(Text.Handle, S[1], 4096);
    if Assigned(OldFlushFunc) then
      Result:= OldFlushFunc(Text);
  end;

initialization
  SetLength(S, 4096);
  Rewrite(Output);
  //TTextRec(Output).InOutFunc := @NewWrite;
  OldFunc:= TTextRec(Output).InOutFunc;
  OldFlushFunc:= TTextRec(Output).FlushFunc;
  TTextRec(Output).InOutFunc := @NewWrite;
  TTextRec(Output).FlushFunc := @NewWrite;
  //Output := Output;

finalization

end.

(*
uses
  sysutils, classes;

type
  TWriteThread = class(TThread)
  protected
    procedure Execute; override;
  end;
  
{TWriteThread}

var
  S: string;
  
procedure _write(s: string);
begin

end;
  
procedure TWriteThread.Execute;
begin
  //_write = system.write;
  //system.write := _write;
  repeat
  Sleep(100);
  if IsConsole then
    write(S);
  until false;
end;
  
 var
   T: TWriteThread;

initialization
  SetLength(S, 4096);
  T:= TWriteThread.Create(true);

finalization
  T.Free;

end.
*)