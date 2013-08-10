program test;

uses
   sysutils, pd_console;
{$M delphi}

//{$I zglCustomConfig.cfg}

//uses
  //zglSpriteEngine;
//  jni;
{
procedure WriteLn(s: string);
begin
  SetLength(s, 4096);
  system.WriteLn(s);
end;
}
var
  s: string;
  i: integer;
begin
  for i:= 0 to paramCount do
    WriteLn(ParamStr(i));
  WriteLn('Hello, what is your name?');
  ReadLn(s);
  for i:= 0 to 2 do
  begin
    Sleep(1000);
    WriteLn(intToStr(i));
  end;
  //ReadLn(s);
  WriteLn('Hello, ' + s);
  //ReadLn(s);
  WriteLn('yes, ' + s);
end.